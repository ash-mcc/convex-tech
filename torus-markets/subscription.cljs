(ns subscription
  "Re-frame 'subscriptions'."
  (:require [re-frame.core :as rf]))


(rf/reg-sub :status (fn [db] (:status db)))
(rf/reg-sub :markets (fn [db] (:markets db)))


(defn listen
  [query-v]
  @(rf/subscribe query-v))


