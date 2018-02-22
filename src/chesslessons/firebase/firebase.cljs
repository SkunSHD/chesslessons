(ns chesslessons.firebase
	(:require
		[reagent.core :refer [atom cursor]]
		))

(def log (.-log js/console))

(def firebase js/firebase)


(defn auth []
	(.auth firebase))


(defn sign_in_with_email_and_password [email password]
	(.signInWithEmailAndPassword (auth) email password))


(defn sign_out []
	(.signOut (auth)))

; ==================
; Facebook
(def facebook_auth_provider (new (.-FacebookAuthProvider (.-auth firebase))))


(defn facebook_link_user [user credential]
	(.then (.link user credential) (fn [wtf] (log "VSE" wtf))))


(defn facebook_fect_provider_for_email [email credential]
	(.then (.fetchProvidersForEmail (auth) email)
	       (fn [providers]
		       (.then (.signInWithEmailAndPassword (auth) email "ward121314")
		              (fn [user] (facebook_link_user user credential))))))


(defn facebook_auth_error [error]
	(log "response" error)
	(if (=(.-code error) "auth/account-exists-with-different-credential")
		(log "[CURE]: delete [user] in [firebase/authentification/users] with same email")))


(defn facebook_auth [than]
	(.catch (.then
		  (.signInWithPopup (auth) facebook_auth_provider) than)
	        facebook_auth_error))


; ==================
; Google
(def google_auth_provider (new (.-GoogleAuthProvider (.-auth firebase))))


(defn google_auth_error [error]
	(log error)
	(if (=(.-code error) "auth/account-exists-with-different-credential")
		(facebook_fect_provider_for_email (.-email error) (.-credential error))))


(defn google_auth [callback]
	(.catch (.then
		(.signInWithPopup (auth) google_auth_provider)
			(fn[new_user]
				(set!
					(.-google_link (.-profile (.-additionalUserInfo new_user)))
					(str "https://plus.google.com/" (aget new_user "additionalUserInfo" "profile" "id")))
				(callback new_user)))
			google_auth_error))