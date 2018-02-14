(ns chesslessons.admin-model
	(:require
		[chesslessons.firebase :as fbs]
;		Utils
		[chesslessons.atom.utils  :refer [atom! action!]]
		[chesslessons.normalize-user.utils :refer [normalize_user]]
))


(def log (.-log js/console))


; ==================
; Atoms
(defonce admin (atom! "[admin.model/admin]" nil))
(defonce sign_in_error_msg (atom! "[admin.model/sign_in_error_msg]" ""))


; ==================
; Actions
(defn set_sign_in_error_msg [new_msg]
	(action! "[admin.model/set_sign_in_error_msg]" new_msg)
	(reset! sign_in_error_msg new_msg))


(defn set_admin [new_admin]
	(action! "[admin.model/set_admin]" new_admin)
	(reset! admin (normalize_user new_admin)))


(defn sign_in_admin [email password]
	(action! "[admin.model/sign_in_admin]" email password)
	(.catch (.then
	(fbs/sign_in_with_email_and_password email password)
			(fn [new_admin] (set_admin new_admin)))
	        (fn [error] (set_sign_in_error_msg (.-message error)))
	        ))


(defn log_out_admin []
	(action! "[admin.model/log_out_admin]")
	(reset! admin nil))