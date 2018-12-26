import React, { Component } from "react";
import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import ECDHKeyChange from "../../components/ECDHKeyChange";

class Home extends Component {
  render() {
    return this.props.authenticated ? (
      <ECDHKeyChange />
    ) : (
      <div className="text-center">
        <h5>Welcome!</h5>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  authenticated: state.header.authentication.authenticated
});

const mapDispatchToProps = dispatch => ({
  dispatch: action => dispatch(action)
});

export default withRouter(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(Home)
);
