(ns chesslessons.core
    (:require
        [reagent.core :as reagent]
        [chesslessons.firebase :as fbs :refer [firebase]]
        ;		History
        [chesslessons.history :as history :refer [nav!]]
        ;		Layout
        [chesslessons.layout :as layout]
        ;       Models
        [chesslessons.visitor-model :as visitor_model]
        [chesslessons.admin-model :as admin_model]
        ;		Components
        [chesslessons.components.nav.components :as nav_components]
        ))

(def log (.-log js/console))
(enable-console-print!)


; APP ID 1001305193295261
; App Secret 48f41533602ddf5804d81ebbbc447d0e


;; -------------------------
;; Views
(defn render_container []
    [:div
     [nav_components/render_nav]
     [:div.text-center.container
      [layout/render]]
     ])


(defn home-page []
    [render_container])


;; -------------------------
;; Initialize app
(reagent/render-component [home-page] (.getElementById js/document "app"))


(defn on-js-reload []
    ;; optionally touch your app-state to force rerendering depending on
    ;; your application
    ;; (swap! app-state update-in [:__figwheel_counter] inc)
    )

(admin_model/auth_init)