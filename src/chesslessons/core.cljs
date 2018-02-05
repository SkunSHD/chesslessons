(ns chesslessons.core
    (:require
		[reagent.core :as r]
		[chesslessons.firebase :as fbs :refer [firebase]]
;       Models
		[chesslessons.user-model :as user_model]
))

(def log (.-log js/console))



; APP ID 1001305193295261
; App Secret 48f41533602ddf5804d81ebbbc447d0e


;; -------------------------
;; Views
(defn home-page []
	(log "USER!3" (str @user_model/user))
;	(fbs/sign_in_with_email_and_password "andiwillfly@gmail.com", "ward121314")
  [:div
   [:button { :onClick fbs/facebook_auth } "facebook auth"]
   [:h2 "Welcome to Reage321nt3"]])


;; -------------------------
;; Initialize app
(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))


(defn init! []
  (mount-root))
