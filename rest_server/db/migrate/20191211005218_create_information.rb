class CreateInformation < ActiveRecord::Migration[5.2]
  def change
    create_table :information do |t|
      t.string :data_type
      t.string :name

      t.timestamps
    end
  end
end
