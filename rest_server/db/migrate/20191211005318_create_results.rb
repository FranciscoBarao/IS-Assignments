class CreateResults < ActiveRecord::Migration[5.2]
  def change
    create_table :results do |t|
      t.string :data_type
      t.float :value
      t.float :information_id
    end
  end
end
