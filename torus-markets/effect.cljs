(ns effect
  "Re-frame 'effects'.
   Action the described-in-data side effects.
   'Data gets turned into action and the world is mutated.'"
  (:require [re-frame.core :as rf]
            [query]))


(rf/reg-fx
 :fetch-markets
 (fn [_] 
   (query/markets)))



