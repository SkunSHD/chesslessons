(ns chesslessons.firebase
	(:require
		[reagent.core :refer [atom cursor]]
		[chesslessons.firebase.user :refer [upodate_user]]

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


(defn facebook_link_user [visitor credential]
	(.then (.link visitor credential) (fn [wtf] (log "VSE" wtf))))


(defn facebook_fect_provider_for_email [email credential]
	(.then (.fetchProvidersForEmail (auth) email)
	       (fn [providers]
		       (.then (.signInWithEmailAndPassword (auth) email "ward121314")
		              (fn [visitor] (facebook_link_user visitor credential))))))


(defn facebook_auth_error [error]
	(log "response" error)
	(if (=(.-code error) "auth/account-exists-with-different-credential")
		(log "[CURE]: delete [visitor] in [firebase/authentification/visitors] with same email")))


(defn facebook_auth [callback]
	(.catch (.then
		  (.signInWithPopup (auth) facebook_auth_provider) callback)
	        facebook_auth_error))


; ==================
; Google
(def google_auth_provider (new (.-GoogleAuthProvider (.-auth firebase))))


(defn google_auth_error [error]
	(log error)
	(if (=(.-code error) "auth/account-exists-with-different-credential")
        (log "google_auth_error" error)))


(defn google_auth [callback]
	(.catch (.then
		(.signInWithPopup (auth) google_auth_provider) callback)
			google_auth_error))