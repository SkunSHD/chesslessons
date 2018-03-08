(ns chesslessons.components.admin_edit.component
	(:require
;		Components
		[chesslessons.components.admin.admin-edit-form.component :as admin_form_components]
))


(def log (.-log js/console))


(defn render []
	 [:div
	   	[:h1 "admin EDIT container"]
	  	[admin_form_components/render]
	  ])