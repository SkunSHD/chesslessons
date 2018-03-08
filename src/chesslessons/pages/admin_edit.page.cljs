(ns chesslessons.admin_edit.page
	(:require
;		Components
		[chesslessons.components.admin_edit.component :as admin_edit_components]
))

(def log (.-log js/console))


; ==================
; Public
(defn render []
    [admin_edit_components/render])