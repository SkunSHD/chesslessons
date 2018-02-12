(ns chesslessons.admin-model
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase :as fbs]
;		Utils
		[chesslessons.normalize-user.utils :refer [normalize_user]]
;		Models
		[chesslessons.visitors-model :as visitors_model]
))


(def log (.-log js/console))


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
	(reset! admin new_admin)
	(visitors_model/get_visitors)
	)



(defn sign_in_admin [email password]
	(.catch (.then
	(fbs/sign_in_with_email_and_password email password)
			(fn [new_admin] (set_admin (normalize_user new_admin))))
	        (fn [error] (set_sign_in_error_msg (.-message error)))
	        ))

