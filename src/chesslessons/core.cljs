(ns chesslessons.core
    (:require
      [reagent.core :as r]
      [chesslessons.firebase :as fb :refer [firebase]]
))

(def log (.-log js/console))

;; -------------------------
;; Views

(defn home-page []
	(fb/sign_in_with_email_and_password "andiwillfly@gmail.com", "ward121314")
  [:div [:h2 "Welcome to Reage321nt3"]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
