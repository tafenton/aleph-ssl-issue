(ns minimal-example.clj.core
  (:require [aleph.http :as http]
            [clojure.java.browse :refer [browse-url]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]
            [hiccup.page :as h]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults secure-site-defaults]])
  (:import [io.netty.handler.ssl SslContextBuilder]
           [io.netty.handler.ssl.util SelfSignedCertificate]))

;;;
; App state management
;;;

(defonce app-state (atom {:web-server nil}))

;;;
; Web handler
;;;

(defn home-page [_]
  (let [csrf-token (force ring.middleware.anti-forgery/*anti-forgery-token*)]
    (h/html5 [:head
              [:meta {:charset "utf-8"}]
              [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
              [:title "Minimal Example"]
              [:meta {:name "Author" :content "TFenton"}]]
             [:body
              [:div#sente-csrf-token {:data-csrf-token csrf-token}]
              [:div#app "Please wait - loading"]
              [:script {:src "web.js"}]])))

(defn home-handler [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (home-page req)})

(defroutes routes
           (GET  "/"                 req (home-handler req))
           (not-found "<h1>Page not found</h1>"))

;;;
; Web server
;;;

(defn self-signed-ssl-context
  "A self-signed SSL context for servers."
  []
  (let [cert (SelfSignedCertificate.)]
    (.build (SslContextBuilder/forServer (.certificate cert) (.privateKey cert)))))

(def insecure-server-config {:port 3000
                             :join? false
                             :compression? true
                             :shutdown-timeout 0})

(def secure-server-config {:port 443
                           :ssl-context (self-signed-ssl-context)
                           :join? false
                           :compression? true})

(defn stop-server []
  (if-let [server (:web-server @app-state)]
    (do (doto server (.close) (.wait_for_close))
        (swap! app-state assoc :web-server nil))))

(defn start-secure-server []
  (stop-server)
  (let [new-server (http/start-server (wrap-defaults #'routes secure-site-defaults) secure-server-config)]
    (swap! app-state assoc :web-server new-server)))

(defn start-insecure-server []
  (stop-server)
  (let [new-server (http/start-server (wrap-defaults #'routes site-defaults) insecure-server-config)]
    (swap! app-state assoc :web-server new-server)))

;;;
; Main function
;;;

(defn start-dev []
  (start-insecure-server)
  (browse-url "http://localhost:3000"))

(defn start []
  (start-secure-server)
  (browse-url "https://localhost:443"))