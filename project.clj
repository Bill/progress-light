(defproject progress-light "0.1.0-SNAPSHOT"
  :description "throttles clj-progress"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [intervox/clj-progress "0.1.6"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]]
  :main ^:skip-aot progress-light.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
