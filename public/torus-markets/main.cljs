(ns main
  "This is the Convex Torus markets app.
   It is implemented as a single (web)page app 
   written in ClojureScript that expects to be run by the Scittle interpreter
   within a webpage inside a web browser.
   It is over engineered for what it does
   but the idea was to provide a structure for future dev-ing."
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [config]
            [subscription :refer [listen]]
            [event]
            [effect]
            [util]))


(defn- header
  []
  [:header
   [:nav
    [:a {:href "torus-markets.html"} [:img {:alt    "Logo"
                                            :src    "icon.png"
                                            :height "50"}]]
    [:span "Convex Torus markets"]
    [:span \u00A0]]])

(defn- notification
  []
  (let [status            (listen [:status])
        {:keys [ok? msg]} status
        decorator         (if ok? util/green util/red)]
    [:small
     [:header
      [:nav
       [:span \u00A0]
       [:span]
       [:span (decorator msg)]]]]))

(defn- markets
  []
  (let [{:keys [timestamp torus-markets]} (listen [:markets])
        cols                              (concat [[:token-name "left"] [:token-addr "right"]
                                                   [:market-addr "right"]
                                                   [:pool-cvm "right"] [:pool-token "right"]
                                                   [:price "right"] [:buy-quote "right"] [:sell-quote "right"]])
        tdata                             (->> torus-markets
                                               (sort-by :market-addr)
                                               ;; Format the desc as a tooltip.
                                               (map (fn [m] 
                                                      (assoc m :token-name [:span.tooltip {:data-tooltip (:token-desc m)} (:token-name m)])))
                                               ;; Format number values.
                                               (map (fn [m]
                                                      (zipmap (keys m)
                                                              (map #(if (number? %) (util/fmt-num %) %) (vals m))))))]
    [:article
     [:table
      [:thead
       [:tr (for [[k ta] cols]
              [:th {:style {:text-align ta}} (name k)])]]
      [:tbody (for [m tdata]
                [:tr (for [[k ta] cols]
                       [:td {:style {:text-align ta}} (k m)])])]]
     [:p [:small "Convex timestamp: " (util/fmt-date (js/Date. timestamp)) [:br] 
          "Convex API URL: " config/api [:br]
          "Convex info: " [:a {:href   config/info
                               :target "_blank"} config/info]]]]))

(defn root
  []
  [:div
   [header]
   [notification]
   [:main
    [markets]]])

(rdom/render [root]
             (do (println "Convex Torus markets app - starting")
                 (.addEventListener (first (.getElementsByTagName js/document "body")) "click" #(rf/dispatch [:set-status {:ok? true
                                                                                                                           :msg ""}]))
                 (rf/dispatch [:initialise-db])
                 (rf/dispatch [:fetch-markets])
                 (.getElementById js/document "app")))



