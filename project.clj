(defproject mannus "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [http-kit "2.1.16"]
                 [clj-jade "0.1.4"]
                 [cheshire "5.3.1"]]
  :main mannus.handler
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler mannus.handler/app
         :auto-reload? true
         :auto-refresh? true}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
