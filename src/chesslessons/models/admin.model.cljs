(ns chesslessons.admin-model
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase :as fbs]
))


(def log (.-log js/console))

; ==================
; Atoms
(defonce admin (atom nil))
(defonce sign_in_error_msg (atom ""))


; ==================
; Public
(defn set_sign_in_error_msg [new_msg]
	(reset! sign_in_error_msg new_msg))

(defn set_admin [new_admin]
	(reset! admin new_admin))


(defn sign_in_admin [email password]
	(.catch (.then
	(fbs/sign_in_with_email_and_password email password)
			(fn [admin] (set_admin admin)))
	        (fn [error] (set_sign_in_error_msg (.-message error)))
	        ))

