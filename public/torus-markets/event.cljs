(ns event
  "Re-frame 'events'.
   Declarative descriptions (just data) of the required side effects."
  (:require [re-frame.core :as rf]))


(rf/reg-event-db
 :initialise-db
 (fn [_ _]
   {:status {:ok? true
             :msg ""}}))

(rf/reg-event-db
 :set-status
 (fn [db [_ v]]
   (assoc db :status v)))

(rf/reg-event-fx
 :fetch-markets
 (fn [cofx _]
   (let [db (:db cofx)]
     {:fetch-markets db
      :db            db})))

(rf/reg-event-db
 :set-markets
 (fn [db [_ m]]
   (assoc db :markets m)))