(defproject minimal-example "0.1.0-SNAPSHOT"
  :description "Minimal reproducing example of resource locking issue"
  :dependencies [[aleph "0.6.1"]
                 [buddy "2.0.0"] ; not used in the example, but need BouncyCastle on classpath to generate self-signed
                                 ; certificate in JDK15+ (https://github.com/netty/netty/issues/10317)
                 [cider/piggieback "0.5.3"]
                 [cljsjs/react "17.0.2-0"]
                 [cljsjs/react-dom "17.0.2-0"]
                 [com.bhauman/figwheel-main "0.2.18" :exclusions [com.google.code.findbugs/jsr305
                                                                  org.eclipse.jetty/jetty-http
                                                                  org.eclipse.jetty/jetty-io
                                                                  org.eclipse.jetty/jetty-util]]
                 [compojure "1.7.0" :exclusions [ring/ring-core]]
                 [hiccup "2.0.0-alpha2"]
                 [reagent "1.2.0"]
                 [ring "1.9.6"]
                 [ring/ring-defaults "0.3.4"]
                 [org.clojure/clojure "1.12.0-alpha1"]
                 [org.clojure/clojurescript "1.11.60" :exclusions [com.fasterxml.jackson.core/jackson-core]]
                 [org.slf4j/slf4j-nop "2.0.0"]]
  :source-paths ["src"]
  :resource-paths ["resources"]
  :target-path "target/%s"
  :repl-options {:init-ns minimal-example.clj.core
                 :nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
  :aliases {"fig:dev"   ["trampoline" "run" "-m" "figwheel.main" "--build" "dev" "--repl"]})