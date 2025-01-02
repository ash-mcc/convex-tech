(ns proxy
  "An in-process proxy for a remote Convex system.
   NB Assumes a map's keys in a response are always keywords. 
      Also, doesn't parse keyword values in a response but conveys them as strings.
      So don't round-trip non-keyword keys or keyword values.
      Also, account addresses in a response are just numbers - they are without their # syntax.")


(defn query
  [api addr cvx-code handler-fn]
  {:pre [(integer? addr)]}
  (println "query:" addr (pr-str cvx-code))
  (let [body #js  {:address addr
                   :source  (pr-str cvx-code)}]
    (-> (js/fetch (str api "/v1/query")
                  #js {:method "POST"
                       :body   (js/JSON.stringify body)})
        (.then #(.text %))
        (.then #(js/JSON.parse %))
        (.then #(js->clj % :keywordize-keys true))
        (.then #(do (println "query response:" %)
                    (handler-fn %)))
        (.catch #(do (println "query error:" %)
                     (handler-fn {:errorCode "PROXY"
                                  :value     (.-message %)}))))))



