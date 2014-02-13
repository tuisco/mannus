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

(def records (parse-stream (clojure.java.io/reader "./configure.json") (fn [k] (keyword k))))
(defn options [sku] {:timeout 200 
                     :query-params {:sku sku} 
                     :basic-auth [(:username records) (:password records)]})
(defn visit-api [url options]
  (let [{:keys [status headers body error] :as resp} @(http/get url options)]
    (if error "ERROR ERROR JUNGLE IS MASSIVE" body)))

(defroutes app-routes
  (GET "/" [] (jade/render "index.jade" {}))
  (GET "/reviewable" {{trkref :trkref sku :sku} :params}
    (visit-api (str "http://localhost:3001/v4/organisations/" trkref "/reviewable.json") (options sku)))
  (GET "/reviews" {{trkref :trkref sku :sku} :params}
    (visit-api (str "http://localhost:3001/v4/organisations/" trkref "/locale/en-GB/reviews.json") (options sku)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))