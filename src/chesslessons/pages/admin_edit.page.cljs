(ns chesslessons.admin_edit.page
	(:require
;		Components
		[chesslessons.components.admin_edit.components :as admin_edit_components]
))

(def log (.-log js/console))


; ==================
; Public
(defn render []
    [admin_edit_components/render_edit_admin_container])