(ns gpad.db
  (:require
    ["@react-native-firebase/firestore" :as firestore]))

(defonce db (.collection (.default firestore) "songs"))
#_(defonce splash-img (js/require "../../assets/150.png"))


(comment
  {:users [user_id {:username "Met"
                    :artists  [a_id {:artist_ref artist_id  ; or meta
                                     :songs      [s_id {:song_ref song_id ; or meta
                                                        :lyrics   ""}]}]}]}

  {:artists [artist_id {:title "oasis"
                        :genre :britpop
                        :songs [song_id {:title      "wonderwall"
                                         :album      "(What's the Story) Morning Glory?"
                                         :year       1995
                                         :lyrics_ref lyrics_id}]}]
   :lyrics  [lyrics_id {:lyrics ""}]}
  )

(defn get-data []
  #_(.forEach xs (fn [d] (println (.data d))))
  (.then
    (.get db)
    (fn [xs]
      (.forEach xs
                (fn [d]
                  () (.data d)
                  )))

    )
  )
