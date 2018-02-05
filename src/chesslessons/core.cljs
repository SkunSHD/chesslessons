(ns chesslessons.core
    (:require
      [reagent.core :as r]))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to Reage321nt"]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
