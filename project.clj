(defproject testblp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
  				 ;;[com.bloomberglp/blpapi "2.0.0"]
  				 [com.bloomberglp/blpapi "3.8.8"]
  				]
  :main ^:skip-aot testblp.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
