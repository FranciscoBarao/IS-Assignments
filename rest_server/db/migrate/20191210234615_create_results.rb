class CreateResults < ActiveRecord::Migration[5.2]
  def change
    create_table :results do |t|
      t.string :type
      t.float :revenue
      t.float :expense
      t.float :proft
      t.references :information, foreign_key: true

      t.timestamps
    end
  end
end
