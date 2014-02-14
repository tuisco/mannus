/**
 * @jsx React.DOM
 */
 var Review = React.createClass({
  render: function() {
    return (
      <article className="review">
      Hello, world! I am a Review.
      </article>
      );
  }
});
var Nav = React.createClass({
  render: function() {
    return (<a href="#">Reviews of {this.props.retailer}</a>)
  }
});

var SideBar = React.createClass({
  render: function(){
    <div id="info">
      <div id="image">
        <img src="images/product_image.png" />
      </div>
      <div id="score">0</div>
      <div id="out"> out of 10</div>
      <div id="based">(Based on 0 reviews)</div>
    </div> 
  }
});
