; @SOURCE: https://lispcast.com/mastering-client-side-routing-with-secretary-and-goog-history/
(ns chesslessons.history
	(:require
		[secretary.core :as secretary :refer-macros [defroute]]
		[goog.events]
		[reagent.core :as r :refer [atom]]
)
	(:import
		[goog.history Html5History EventType]))


; ==================
; Atoms
(def current_page (atom {:name "home_page" :params {} }))

; ==================
; We need a function that will get the current path fragment to switch on.
; We’ll just use the path and the query string.
(defn get-token []
	(str js/window.location.pathname js/window.location.search))


; ==================
; Now we define how to instatiate the history object.
(defn make-history []
	(doto (Html5History.)
	      (.setPathPrefix (str js/window.location.protocol
	                           "//"
	                           js/window.location.host))
	      (.setUseFragment false)))


; ==================
; Now a handler for what to do when the URL changes.
(defn handle-url-change [e]
	(js/console.groupEnd)
	(js/console.group (str "nav!: " (get-token)))
	(secretary/dispatch! (get-token)))


; ==================
; Now we set up our global history object. We use defonce so we can hot reload the code.
(defonce history (doto (make-history)
                       (goog.events/listen EventType.NAVIGATE
                                           ;; wrap in a fn to allow live reloading
                                           #(handle-url-change %))
                       (.setEnabled true)))

; ==================
; Routes
(defroute home-page "/" []
	(reset! current_page {:name "home_page" :params {}})
	(js/console.log "Homepage!"))

(defroute admin-page "/admin" []
	(reset! current_page {:name "admin_page" :params {}})
	(js/console.log "admin page!"))

(defroute default-route "*" []
	(js/console.log (str "unknown route: " (get-token))))


; ==================
; Public
; And we will want a function to programmatically change the URL (and add to the history).
(defn nav! [path]
	(.setToken history path))