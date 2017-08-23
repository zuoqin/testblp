(ns testblp.core
  (:gen-class)
  (:require
    [clojure.string :as str]
    [dk.ative.docjure.spreadsheet]
    ;;[datomic.api :as d]

    [clj-time.core :as t]
    [clj-time.format :as f]
    [clj-time.local :as l]
    [clj-time.coerce :as c]

    [clojure.data.json :as json]
    [clj-http.client :as client]
  )
  (use dk.ative.docjure.spreadsheet)
  )


(import com.bloomberglp.blpapi.CorrelationID)
(import com.bloomberglp.blpapi.Element)
(import com.bloomberglp.blpapi.Event)
(import com.bloomberglp.blpapi.Event$EventType)
(import com.bloomberglp.blpapi.Message)
(import com.bloomberglp.blpapi.MessageIterator)
(import com.bloomberglp.blpapi.Name)
(import com.bloomberglp.blpapi.Request)
(import com.bloomberglp.blpapi.Service)
(import com.bloomberglp.blpapi.Session)
(import java.util.Calendar)

(defn abs [n] (max n (- n)))

(def build-in-basicdate-formatter (f/formatters :basic-date))

(def drive "c")

(def app-state (atom {}) )

;(def uri "datomic:dev://localhost:4334/sberpb_dev")

; (defn ent [id]
;   (let [
;    conn (d/connect uri)
;   ]
;   (seq (d/entity (d/db conn) (ffirst id))) )
; )

; (defn update-price [security price]
;   (let [
;     conn (d/connect uri)
;     dt (java.util.Date.)
;     dttxt (f/unparse build-in-basicdate-formatter (c/from-long (c/to-long dt)))
;     secid (ffirst (d/q '[:find ?e
;                         :in $ ?bcode
;                         :where
;                         [?e :security/bcode]
;                         [?s :security/bcode ?bcode]
;                         ]
;                       (d/db conn) security))



;     tr1 (println (str "secid=" secid))
;     priceid (d/q '[:find ?e ;?t ?a ?y ?d ?p ?dur
;              :in $ ?sec
;              :where
;              [?e :price/security ?sec]
;              ] (d/db conn) secid)

;     price (ent priceid)
;     target (second (first (filter (fn [x] (if (= (first x) (keyword "price/targetprice")) true false)) price) ))

;     anr (second (first (filter (fn [x] (if (= (first x) (keyword "price/analystrating")) true false)) price) ))

;     yield (second (first (filter (fn [x] (if (= (first x) (keyword "price/yield")) true false)) price) ))

;     dvddate (second (first (filter (fn [x] (if (= (first x) (keyword "price/dvddate")) true false)) price) ))

;     putdate (second (first (filter (fn [x] (if (= (first x) (keyword "price/putdate")) true false)) price) ))

;     duration (second (first (filter (fn [x] (if (= (first x) (keyword "price/duration")) true false)) price) ))

;     tr2 (if (not (nil? priceid)) (d/transact conn [[:db.fn/retractEntity (ffirst priceid)]]))

;     ]
;     (d/transact-async conn  [{ :price/security secid :price/lastprice price :price/valuedate dt  :price/source "Bloomberg API import" :price/comment (str "Import from Bllomberg API output on " dttxt) :price/targetprice target :price/analystrating anr :price/yield yield :price/dvddate dvddate :price/putdate putdate :price/duration duration :db/id #db/id[:db.part/user -100001 ]}])
;   )

; )

(defn map-sec [sec]
  {:acode (get sec "acode") :bcode (get sec "bcode") :price (get sec "price") :currency (get sec "currency") :multiple (get sec "multiple") }
)

(defn get-securities []
  (let [


    ]
    (client/get "http://10.20.35.21:3000/api/security"
      {;:proxy-host "10.20.30.41"
       ;:proxy-port 8080
       :headers {
         "Proxy-Authorization" (str "Basic bXNrXGF6b3JjaGVua292OkxldW1pMjA1")
         "authorization" "Bearer eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJpc3MiOiJ6dW9xaW4iLCJleHAiOjE1MDMzOTI1MTcsImlhdCI6MTUwMzMwNjExN30."}
       :content-type :json
       ;:socket-timeout 1000  ;; in milliseconds
       ;:conn-timeout 1000    ;; in milliseconds
       }
    )
  )
)

(defn update-price-http [security price]
  (let [


    ]
    (client/get "http://10.20.35.21:3000/api/postran?client=AANDF&security=17592186045502"
      {;:proxy-host "10.20.30.41"
       ;:proxy-port 8080
       :headers {
         "Proxy-Authorization" (str "Basic bXNrXGF6b3JjaGVua292OkxldW1pMjA1")
         "authorization" "Bearer eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJpc3MiOiJ6dW9xaW4iLCJleHAiOjE1MDMzOTI1MTcsImlhdCI6MTUwMzMwNjExN30."}
       :content-type :json
       ;:socket-timeout 1000  ;; in milliseconds
       ;:conn-timeout 1000    ;; in milliseconds
       }
    )
  )
)

; (defn get-price [bcode]
;   (let [
;         conn (d/connect uri)
;         price (ffirst (d/q '[:find ?v
;                         :in $ ?bcode
;                         :where
;                         [?s :security/bcode ?bcode]
;                         [?p :price/lastprice ?v]
;                         [?p :price/security ?s]
;                         ]
;                       (d/db conn) bcode)) 

;     ]
;     price
;   )
; )

(defn value-map [f m]
  (zipmap (keys m) (map f (vals m))))

(defn value-filter [f m]
  (into {}
        (for [[k v] m :when (f v)]
          [k v])))

(defn reduce-map
  ([vs] (reduce-map {} vs))
  ([m vs]
   (reduce
    (fn [m v]
      (assoc-in m (pop v) (peek v))) m vs)))

(defn date []
  (let [
        cal (Calendar/getInstance)
        ]
    [(.get cal Calendar/YEAR) (inc (.get cal Calendar/MONTH)) (.get cal Calendar/DATE)]))

(defn new-session []
  (doto (Session.)
    .start
    (.openService "//blp/refdata")))

(defonce session (atom 0))

(defmacro add-items [items element-name]
  `(let [securities-el# (.getElement ~'request ~element-name)]
     (doseq [item# ~items]
       (.appendValue securities-el# item#))))

(def session-lock (Object.))

(defn date->str [[year month date]]
  (format "%04d%02d%02d" year month date))

(defn filter-messages [event correlation-id]
  (filter #(= correlation-id (.correlationID %)) (iterator-seq (.messageIterator event))))

(defn assoc-in-last [m [k & rest] v]
  (if rest
    (let [
          k (if (vector? m) (dec (count m)) k)
          ]
      (assoc m k (assoc-in-last (get m k) rest v)))
    (if (vector? m)
      (conj m v)
      (assoc m k v))))

(defn get-v [line]
  (let [
        v (-> line (.split " = ") second)
        ]
    (if v
      (try
        (Long/parseLong v)
        (catch Exception e
          (try
            (Double/parseDouble v)
            (catch Exception e
              (.replace v "\"" ""))))))))

(defn parse-response [response]
  (let [
    data (str response)
    tr1 (println (str response))
    ]

    (loop [pos1 0 pos2 0 result {}]
      (let [
        newpos1 (str/index-of data "security = \"" pos1)
        
        newpos2 (str/index-of data "\n" (+ (if (nil? newpos1) pos1 newpos1) 1))
        ;tr1 (println (str "pos1=" newpos1 " pos2=" newpos2))

        security (subs data (+ newpos1 12) (- newpos2 3))
        tr1 (println (str "security=" security) )

        newpos1 (str/index-of data "PX_LAST = " (+ newpos2 10))
        newpos2 (str/index-of data "\n" (+ (if (nil? newpos1) pos1 newpos1) 1))

        ;tr1 (println (str "pos1=" newpos1 " pos2=" newpos2))

        ;;pos2 1;;(+ pos1 10)
        price (subs data (+ newpos1 10) (- newpos2 1))

        prevprice (get-price security)

        ;tr1 (if (nil? prevprice) (println (str "security=" security " price=" price " prevprice=" prevprice))) 

        ;tr1 (if (> (/ (abs (- (. Double parseDouble price) prevprice)) prevprice) 0.001) (update-price security price))

        ]
        (if (or (nil? (str/index-of data "security = \"" newpos1))) result (recur newpos1 newpos2 {}))
      )    
    )
  )
  (println "Success")
  ;(println (str "response=" (clojure.string/replace (str response) #"ReferenceDataResponse =" "")))  ;;(first (clojure.string/split (str response) #"^.*PX_LAST.*$"))
  ;; (loop [
  ;;        lines (->> response str clojure.string/split-lines (drop 1) drop-last)
  ;;        done {}
  ;;        stack []
  ;;        ]
  ;;   (if (empty? lines)
  ;;     done
  ;;     (let [
  ;;           line (first lines)
  ;;           ;            k (re-find #"[a-zA-Z_\[\]0-9]+" line)
  ;;           k (-> line (.split " = ") first .trim)
  ;;           new-stack (conj stack k)
  ;;           ]
  ;;       (cond
  ;;        (.contains line "[]")
  ;;        (recur (rest lines) (assoc-in-last done new-stack []) new-stack)
  ;;        (.contains line "{")
  ;;        (if (and (not-empty stack) (.contains (peek stack) "[]"))
  ;;          (recur (rest lines) (assoc-in-last done new-stack {}) new-stack)
  ;;          (recur (rest lines) done new-stack))
  ;;        (.contains line "}")
  ;;        (recur (rest lines) done (pop stack))
  ;;        :default
  ;;        (recur (rest lines) (assoc-in-last done new-stack (get-v line)) stack)))))
   ;(json/read-str (clojure.string/replace (str response) #"ReferenceDataResponse =" "") :key-fn keyword)
)

(defn request [securities fields & [start-date end-date periodicity]]
  ;periodicity is DAILY WEEKLY MONTHLY QUARTERLY SEMI_ANNUALLY YEARLY
  (locking session-lock
    (let [
          checksession(if (= @session 0) (reset! session (new-session)))
          service (.getService @session "//blp/refdata")
          request-type (if start-date "HistoricalDataRequest" "ReferenceDataRequest")
          request (.createRequest service request-type)
          ] (try
      
          (add-items securities "securities")
          (add-items fields "fields")
          (when start-date
            (.set request "startDate" (date->str start-date))
            (.set request "endDate" (date->str end-date))
            (.set request "periodicitySelection" periodicity)
            (.set request "adjustmentSplit" false)
            (.set request "adjustmentFollowDPDF" false)
            (.set request "adjustmentNormal" false)
            (.set request "adjustmentAbnormal" false)
            )
          (let [
                ;;tr1 (println "request1")
                correlation-id (.sendRequest @session request nil)
                ;;tr1 (println "request2")
                ]
            (loop [
                   done []
                   ]
              (let [
                    event (.nextEvent @session)
                    done (concat done (filter-messages event correlation-id))
                    ]
                (if (= Event$EventType/RESPONSE (.eventType event))
                  (map parse-response done)
                  (recur done)))))
          (catch Exception e
            (reset! session (new-session))
            (request securities fields start-date end-date periodicity))))))

(defn latest-request [securities fields]
  (reduce-map
   (for [
         response (request securities fields)
         security-data (if (nil? response) [] (response "securityData[]")) 
         ]
     [(security-data "security") (security-data "fieldData")])))



;;(def companies (.split (slurp "resources/companies2.csv") "\n"))

(def companies
  (let [secs (drop 0 (->> (load-workbook (str drive ":/DEV/clojure/sberpb/sberapi/DB/quotes.xlsx"))
                   (select-sheet "Data")
                   (select-columns {:B :bcode})))
    ]
    (map (fn [x] (str (:bcode x) "\r") ) secs)
  )
)


(sort companies)

(def c (take 2 companies))

(defn get-des [{c "PX_LAST[]"}]
  (apply str
         (interpose "\n"
                    (map #(-> % first second) c))))

(defn get-descriptions [companies]
  ;;(value-map get-des (latest-request companies ["PX_LAST"]))
  (latest-request companies ["PX_LAST"])

)

(defn loadprices []
  (let [
    secs (map map-sec (json/read-str (:body (get-securities)) ))
    tr1 (swap! app-state assoc-in [:secs] secs)


    ;;allsecs (sort (comp sort-securities) (secs/get-securities))
    secs (map (fn [x] (:isin x)) (drop 0 (->> (load-workbook (str (-> env :drive) ":/DEV/clojure/sberpb/sberapi/DB/quotes.xlsx") )
                               (select-sheet "Data")
                               (select-columns {:A :isin})))) 

    securities (filter (fn [x] (some  #(= (:isin x) %) secs)) allsecs)

    ]
    (doseq [client clients]
      (println (str "retrieving positions for portfolio: " (:code client)))
      (getPositions "" (:code client))
      (Thread/sleep 3000)
    )

    (doseq [client clients]
      (println (str "retrieving deals for portfolio: " (:code client)))
      (getDeals "" (:code client) 0)
      (Thread/sleep 3000)
    )

    (doseq [sec securities]
      (println (str "retrieving portfolios for security: " (:acode sec)))
      (getPortfolios "" (:id sec))
      (Thread/sleep 3000)
    )

    (doseq [sec securities]
      (println (str "calculating portfolios limits for security: " (:acode sec)))
      (calcPortfolios "" (:id sec) 10.0)
      (Thread/sleep 3000)
    )

    (println "Finished caching data")
    ;(first securities)
  )
)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
)
