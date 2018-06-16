(ns status-im.chat.views.api.choose-asset
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [re-frame.core :as re-frame]
            [status-im.ui.components.list.views :as list]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.colors :as colors]))

(defn- render-asset [arg-index bot-db-key]
  (fn [asset]
    [react/touchable-highlight {:on-press #(re-frame/dispatch
                                            [:set-asset-as-command-argument {:arg-index  arg-index
                                                                             :bot-db-key bot-db-key
                                                                             :asset      asset}])}
     [react/view {:flex-direction   :row
                  :align-items      :center
                  :padding-vertical 11}
      [react/image {:source (-> asset :icon :source)
                    :style  {:width 30
                             :height 30
                             :margin-left 14
                             :margin-right 12}}]
      [react/text {:style {:color :black}} (:symbol asset)]
      [react/text {} " "]
      [react/text {:style {:color :gray}} (:name asset)]]]))

(def assets-separator [react/view {:height           1
                                   :background-color colors/gray-light
                                   :margin-left      56}])

(defview choose-asset-view [{title      :title
                             arg-index  :index
                             bot-db-key :bot-db-key}]
  (letsubs [assets [:wallet/visible-assets]]
    [react/view
     [react/text {:style {:font-size      14
                          :color          "rgb(147, 155, 161)"
                          :padding-top    12
                          :padding-left   16
                          :padding-right  16
                          :padding-bottom 12}}
      title]
     [list/flat-list {:data                      assets
                      :key-fn                    (comp name :symbol)
                      :render-fn                 (render-asset arg-index bot-db-key)
                      :enableEmptySections       true
                      :separator                 assets-separator
                      :keyboardShouldPersistTaps :always
                      :bounces                   false}]]))