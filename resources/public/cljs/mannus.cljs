(ns mannus
  (:require [domina :as dom]
            [domina.events :as ev]))
(defn ^:export init []
  (ev/listen! (dom/by-id "customer_reviews") :click 
    (fn [ev] (do (js/alert "Please, complete the form!") false))))
(set! (.-onload js/window) init)
