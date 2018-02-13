(ns chesslessons.visitors-model
    (:require
	    [reagent.core :refer [atom]]
        [chesslessons.firebase.db :as db]
;		Utils
        [chesslessons.normalize-user.utils :refer [normalize_user]]))


(def log (.-log js/console))


; ==================
; Atoms
(defonce visitors (atom {}))
(defonce visitors_error_msg (atom ""))



; ==================
; Private
(defn- -format_visitors [visitors]
	(map (fn [visitor] (js->clj (.data visitor) :keywordize-keys true)) (aget visitors "docs")))



; ==================
; Public
(defn set_visitors [new_visitors]
    (log "set visitors" new_visitors)
    (reset! visitors new_visitors))


(defn set_visitors_error_msg [errors]
  (log "set_visitors error" errors)
  (reset! visitors_error_msg errors))


(defn get_visitors []
    (log "Get -visitors")
  (.catch (.then (db/get_user_all)
                  (fn [visitors] (set_visitors(-format_visitors visitors))))
                  (fn [error] set_visitors_error_msg (.-message error))))