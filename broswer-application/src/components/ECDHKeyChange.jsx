import React from "react";
import { Row, Col, Input, Label, Badge, FormFeedback } from "reactstrap";

const { ecdhParams, curveParams } = require("../utils/ecdhHelpers");

const origin = {
  random: "",
  pubKey: {},
  secret: {},
  pubKeyWarning: "",
  secretWarning: ""
};

class ECDHKeyChange extends React.Component {
  constructor(props) {
    super(props);
    this.state = { ...origin };
  }

  onComputePublic() {
    if (this.state.random) {
      this.setState({
        pubKey: ecdhParams.aPub,
        pubKeyWarning: ""
      });
    } else {
      this.setState({ pubKeyWarning: "Generate random first" });
    }
  }

  onDeriveSecret() {
    if (!this.state.random) {
      this.setState({ secretWarning: "Generate random first" });
    } else if (!Object.keys(this.state.pubKey).length) {
      this.setState({ secretWarning: "Compute public first" });
    } else {
      this.setState({
        secret: ecdhParams.aKey,
        secretWarning: ""
      });
    }
  }

  render() {
    const { random, pubKey, secret, pubKeyWarning, secretWarning } = this.state;

    return (
      <Col xs="12" md={{ size: 10, offset: 1 }} lg={{ size: 8, offset: 2 }}>
        <h4>ECDH Key Exchange</h4>
        <Col className="base-margin-top">
          <span>Step 1: Private value </span>
          <Badge
            color="info"
            className="general-button"
            onClick={() => this.setState({ random: ecdhParams.aPri })}
          >
            Generate Random
          </Badge>
          <Input value={random} />
        </Col>

        <Col className="base-margin-top">
          <span>Step 2: Public point (A= aG)(X,Y): </span>
          <Badge
            color="info"
            className="general-button"
            onClick={() => this.onComputePublic()}
          >
            Compute Public
          </Badge>
          <Input value={pubKey.X} invalid={pubKeyWarning} />
          <Input value={pubKey.Y} invalid={pubKeyWarning} />
          <FormFeedback> {pubKeyWarning} </FormFeedback>
        </Col>

        <Col className="base-margin-top base-margin-bottom">
          <span>Step 3: Secret key (S = aB = abG) (X,Y): </span>
          <Badge
            color="info"
            className="general-button"
            onClick={() => this.onDeriveSecret()}
          >
            Derive Secret
          </Badge>
          <Input value={secret.X} invalid={secretWarning} />
          <Input value={secret.Y} invalid={secretWarning} />
          <FormFeedback> {secretWarning} </FormFeedback>
        </Col>

        <Label size="lg">Elliptic Curve parameters </Label>
        <Badge color="primary">secp256r1</Badge>
        {Object.keys(curveParams).map(key => {
          const value = curveParams[key];
          return (
            <Row className="half-margin-bottom">
              <Label className="text-right" xs="1">
                {key}:
              </Label>
              <Col>
                <Input value={value} disabled />
              </Col>
            </Row>
          );
        })}
      </Col>
    );
  }
}

export default ECDHKeyChange;
