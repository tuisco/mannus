(ns mannus.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [org.httpkit.client :as http]
            [clj-jade.core :as jade]
            [cheshire.core :refer :all]))

(jade/configure {:template-dir "views/"
                 :pretty-print true
                 :cache? true})
(def records (
  parse-stream (clojure.java.io/reader "./configure.json") (fn [k] (keyword k))))

(defn url-options
  ([] 
    {:timeout 1000 :basic-auth [(:username records) (:password records)]})
  ([sku] 
    {:timeout 1000 :query-params {:sku sku} :basic-auth [(:username records) (:password records)]}))

(defn visit-api [url options]
  (let [{:keys [status headers body error] :as resp} @(http/get url options)]
    (if error "{}" body)))

(defroutes app-routes
  (GET "/" {{trkref :trkref sku :sku} :params} (jade/render "index.jade" { :sku sku :trkref trkref }))
  (GET "/reviewable" {{trkref :trkref sku :sku} :params}
    (visit-api (str "http://rw-api.reevoocloud.com/v4/organisations/" trkref "/reviewable.json") (url-options sku)))
  (GET "/reviews" {{trkref :trkref sku :sku} :params}
    (visit-api (str "http://rw-api.reevoocloud.com/v4/organisations/" trkref "/locale/en-GB/reviews.json") (url-options sku)))
  (GET "/organisation" {{trkref :trkref} :params}
    (visit-api (str "http://rw-api.reevoocloud.com/v4/organisations/" trkref ".json") (url-options)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> #'app-routes handler/site))