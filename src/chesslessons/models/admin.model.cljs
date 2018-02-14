(ns chesslessons.admin-model
	(:require
		[chesslessons.firebase :as fbs]
;		Utils
		[chesslessons.atom.utils  :refer [atom!]]
		[chesslessons.normalize-user.utils :refer [normalize_user]]
))


(def log (.-log js/console))


; ==================
; Atoms
(defonce admin (atom! "[admin.model/admin]" nil))
(defonce sign_in_error_msg (atom! "[admin.model/sign_in_error_msg]" ""))


; ==================
; Public
(defn set_sign_in_error_msg [new_msg]
	(reset! sign_in_error_msg new_msg))

(defn set_admin [new_admin]
	(reset! admin (normalize_user new_admin))
	)



(defn sign_in_admin [email password]
	(.catch (.then
	(fbs/sign_in_with_email_and_password email password)
			(fn [new_admin] (set_admin new_admin)))
	        (fn [error] (set_sign_in_error_msg (.-message error)))
	        ))

