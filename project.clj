(defproject testblp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ;:repositories {"my.datomic.com" {:url "https://my.datomic.com/repo" :creds :gpg}}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 ;[com.datomic/datomic-pro "0.9.5350" :exclusions [org.slf4j/log4j-over-slf4j org.slf4j/slf4j-nop]]
                 [com.bloombergblp/blpapi "3.8.8.2"]
                 [dk.ative/docjure "1.11.0"]
                 [clj-time "0.13.0"]
                 [clj-http "3.7.0"]
                 [org.clojure/data.json "0.2.6"]
                ]
  :main ^:skip-aot testblp.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
