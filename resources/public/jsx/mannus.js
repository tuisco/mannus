/**
 * @jsx React.DOM
 */
$(function() {
  var RetailerName = React.createClass({
    loadRetailerFromServer: function() {
      $.ajax({
        url: '/organisation',
        data: { trkref : this.props.trkref },
        dataType: 'json',
        success: function(data) {
          this.setState({retailer: data.friendly_name});
        }.bind(this)
      });
    },
    getInitialState: function() {
      return {retailer: "(retailer)"};
    },
    componentWillMount: function() {
      this.loadRetailerFromServer();
    },
    render: function() {
      return (<a href="#">Reviews of {this.state.retailer}</a>);
    }
  });

  var InfoObject = React.createClass({
    render : function() {
      return (<span> {this.props.data}</span>);
    }
  });

  var Image = React.createClass({
    render : function() {
      return (<img src={this.props.url} />);
    }
  });

  var Review = React.createClass({
    render : function(){
      return (
        <div className="review">
          {this.props.data.good_points}
        </div>
      );
    }
  });

  var ReviewList = React.createClass({
    render: function() {
      var reviewNodes = this.props.data.reviews.map(function (review) {
        return <Review data={review} />;
      });
      return (
        <div className="review-list">
          {reviewNodes}
        </div>
      );
    }
  });

  React.renderComponent(
    <RetailerName trkref={trkref} />,
    document.getElementById('retailer-name')
  );

  $.ajax({
    url: '/reviews',
    data: { sku: sku, trkref: trkref},
    dataType: 'json',
    success: function(data) {
      React.renderComponent(
        <ReviewList data={data} />,
        document.getElementById('reviews')
      );
      var scoreObject = data.summary.facets.filter(function(segment){
        return segment.tag == "overall"
      });
      var score = (scoreObject[0].statistics.summation / scoreObject[0].statistics.respondents).toFixed(1);
      React.renderComponent(<InfoObject data={score} />, document.getElementById('score'));

      var count = data.summary.pagination.total_entries;
      React.renderComponent(<InfoObject data={count} />, document.getElementById('based'));

      React.renderComponent(<Image url={data.summary.parent.reviewable.image_url} />, document.getElementById('image'));

    }.bind(this)
  });
});
