(defproject org.hypercrud/seattle-service "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.datomic/datomic-free "0.9.5067" :exclusions [org.slf4j/slf4j-nop]]
                 [datomico "0.2.0"]
                 [org.hypercrud/hypercrud-service "0.1.0-SNAPSHOT"]
                 [io.pedestal/pedestal.service "0.3.1"]
                 [io.pedestal/pedestal.service-tools "0.3.1"]
                 [io.pedestal/pedestal.jetty "0.3.1"]
                 [ns-tracker "0.2.1"]
                 [ch.qos.logback/logback-classic "1.1.2" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.7"]
                 [org.slf4j/jcl-over-slf4j "1.7.7"]
                 [org.slf4j/log4j-over-slf4j "1.7.7"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "service.server/run-dev"]}
                   :dependencies [[io.pedestal/pedestal.service-tools "0.3.1"]
                                  [org.clojure/tools.namespace "0.2.3"]]
                   :source-paths ["dev"]}}
  :repl-options {:init-ns user}
  :main ^{:skip-aot true} streaker-service.server
  )
