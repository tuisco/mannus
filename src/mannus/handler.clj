(ns mannus.handler
  (:use compojure.core)
  (:use ring.middleware.reload)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [org.httpkit.client :as http]
            [clj-jade.core :as jade]))

(jade/configure {:template-dir "views/"
                 :pretty-print true
                 :cache? true})

(defroutes app-routes
  (GET "/" {{trkref :trkref sku :sku} :params}
    ((def options {:timeout 200  ; ms
                  :basic-auth ["" ""]
                  :query-params {:param "value" :param2 ["value1" "value2"]}
                }
    )
    (http/get (str "http://localhost:3001/v4/organisations/" trkref "/reviewable.json?sku=" sku) options
              (fn [{:keys [status headers body error]}] ;; asynchronous handle response
                (if error
                  (println "Failed, exception is " error)
                  (println "Async HTTP GET: " body))))
    (http/get (str "http://localhost:3001/v4/organisations/" trkref "/locale/en-GB/reviews.json?sku=" sku) options
              (fn [{:keys [status headers body error]}] ;; asynchronous handle response
                (if error
                  (println "Failed, exception is " error)
                  (println "Async HTTP GET: " body)))))
    (jade/render "index.jade" {}))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
