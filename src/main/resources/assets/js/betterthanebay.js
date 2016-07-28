/**
 * Created by yoganandc on 7/28/16.
 */

$(function() {

    var ItemModel = Backbone.Model.extend({ url: '/api/items/16'});
    var ItemView = Backbone.View.extend({
                                            el: '#item-view',
                                            template: _.template($('#item-template').html()),

                                            initialize: function() {
                                                this.listenTo(this.model, 'sync change', this.render);
                                                this.model.fetch({
                                                    success: function(model, response, options) {
                                                        console.log('success');
                                                    },
                                                    error: function(model, response, options) {
                                                        console.log('failure');
                                                    }
                                                });
                                                this.render();
                                            },

                                            render: function() {
                                                var html = this.template(this.model.toJSON());
                                                this.$el.html(html);
                                                return this;
                                            }
                                        });

    var item16 = new ItemModel();
    item16.credentials = {username: 'yoganandc', password: 'password3'};
    var item16View = new ItemView({model: item16});

    // var ItemsModel = Backbone.Model.extend({});
    // var ItemCollection = Backbone.Collection.extend({
    //                                               url: '/api/items',
    //                                               model: ItemsModel
    //                                           });
    // var ItemsView = Backbone.View.extend({
    //                                         el: '#item-list',
    //                                         initialize: function() {
    //                                             console.log(this.collection);
    //                                         }
    //                                     });
    //
    // var itemCollection = new ItemCollection();
    // var itemsView = new ItemsView({ collection: itemCollection });
});
