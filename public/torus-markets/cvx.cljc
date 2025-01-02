(ns cvx
  "Convex Lisp code - represented in quoted Clojure code.")


(def markets
  "Queries for data about the Torus markets.
   TODO: Pagination."
  '(do
     (import convex.asset :as asset)
     (import torus.exchange :as torus)
     {:timestamp     *timestamp*
      :torus-markets (map (fn [tok]
                            (let [mkt (torus/get-market tok)]
                              {:token-addr  tok
                               :token-name  (:name (call *registry* (lookup tok)))
                               :token-desc  (or (:desc (call *registry* (lookup tok)))
                                                (:description (call *registry* (lookup tok))))
                               :market-addr mkt
                               :pool-cvm    (balance mkt)
                               :pool-token  (asset/balance tok mkt)
                               :price       (torus/price tok)
                               :buy-quote   (torus/buy-quote tok 1)
                               :sell-quote  (torus/sell-quote tok 1)}))
                          (keys torus/markets))}))

