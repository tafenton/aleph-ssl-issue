(ns ^:figwheel-hooks minimal-example.cljs.web
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]))

(defn simple-component []
  [:div
   [:p "I am a component!"]
   [:p.someclass
    "I have " [:strong "bold"]
    [:span {:style {:color "red"}} " and red "] "text."]])

(defn ^:after-load re-render []
  (rdom/render [simple-component] (.getElementById js/document "app")))

(re-render)