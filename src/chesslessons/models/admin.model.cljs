(ns chesslessons.admin-model
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase :as fbs]
;		Utils
		[chesslessons.normalize-user.utils :refer [normalize_user]]
))


(def log (.-log js/console))
(defn cljs [object] (js->clj object :keywordize-keys true))
(defn cljs_parse [string] (js->clj (.parse js/JSON string) :keywordize-keys true))


; ==================
; Atoms
(defonce admin (atom nil))
(defonce sign_in_error_msg (atom ""))


; ==================
; Cursors
(def admin_uid (cursor admin [:uid]))
(def admin_email (cursor admin [:email]))



; ==================
; Public
(defn set_sign_in_error_msg [new_msg]
	(reset! sign_in_error_msg new_msg))

(defn set_admin [new_admin]
	(reset! admin new_admin))


(defn sign_in_admin [email password]
	(.catch (.then
	(fbs/sign_in_with_email_and_password email password)
			(fn [new_admin] (set_admin (normalize_user new_admin))))
	        (fn [error] (set_sign_in_error_msg (.-message error)))
	        ))
