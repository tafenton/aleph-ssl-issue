(ns ^:figwheel-hooks minimal-example.cljs.web
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [taoensso.sente :as sente]))

(defn simple-component []
  [:div
   [:p "I am a component!"]
   [:p.someclass
    "I have " [:strong "bold"]
    [:span {:style {:color "red"}} " and red "] "text."]])

(def ?csrf-token
  (when-let [el (.getElementById js/document "sente-csrf-token")]
    (.getAttribute el "data-csrf-token")))

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket-client!
        "/chsk" ; Note the same path as before
        ?csrf-token
        {:type :auto})] ; e/o #{:auto :ajax :ws}
  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state))   ; Watchable, read-only atom

(defn ^:after-load re-render []
  (rdom/render [simple-component] (.getElementById js/document "app")))

(re-render)