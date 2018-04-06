(ns chesslessons.visitors-model
    (:require
        [chesslessons.firebase.db :as db]
;		Utils
        [chesslessons.atom.utils :refer [atom! action!]]
        [chesslessons.normalize-visitor.utils :refer [normalize_visitor format_visitors]]
        ))


(def log (.-log js/console))


; ==================
; Atoms
(defonce visitors (atom! "[visitors.model/visitors]" '()))
(defonce deleted_visitors (atom! "[visitors.model/deleted_visitors]" '()))


; ==================
; Private
(defn- -format_visitors [visitors]
    (map (fn [visitor] (js->clj (.data visitor) :keywordize-keys true))
         (aget visitors "docs")))


; ==================
; Public
(defn set_visitors [new_visitors]
    (action! "[visitors.model/set_visitors]" new_visitors)
    (reset! visitors new_visitors)
    )