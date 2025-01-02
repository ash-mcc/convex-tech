(ns util
  (:require [clojure.string :as str]
            [cljs.pprint :refer [cl-format]]))

(defn- truncate 
  [x]
  (if (neg? x)
    (js/Math.ceil x)
    (js/Math.floor x)))
 
(defn fmt-num 
  [x]
  (let [int-part (truncate x)
        dec-part (- x int-part)]
    (cl-format nil "~:d~0,2f" int-part dec-part)))

(defn red
  [s]
  [:span {:style {:color "red"}} s])

(defn green
  [s]
  [:span {:style {:color "green"}} s])

(defn fmt-date
  [date]
  (str/replace (.toISOString date) #"\.[0-9]{3}" ""))

(defn- status
  [ok? msg]
  {:ok? ok?
   :msg (str (fmt-date (js/Date.)) " " msg)})

(defn ok-status
  [msg]
  (status true
          msg))

(defn err-status
  [m]
  (status false
          (str "Error " (:errorCode m) " " (:value m))))