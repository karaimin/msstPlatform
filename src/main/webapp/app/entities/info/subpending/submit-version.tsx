import { RouteComponentProps } from 'react-router';
import React from 'react';
import { text } from '@fortawesome/fontawesome-svg-core';
import { Storage } from 'react-jhipster';

export interface ISubmitVersionState {
  text?: string;
}

export interface IVersionProps extends RouteComponentProps<{ id: string }> {
  lineId?: string;
}

export default class SubmitVersionForm extends React.Component<IVersionProps, ISubmitVersionState> {
  constructor(props) {
    super(props);
    this.state = {
      text: ''
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange = event => {
    this.setState({ text: event.target.value });
  };

  handleSubmit = event => {
    event.preventDefault();

    const versionBody = {
      text: this.state.text
    };

    const data = JSON.stringify(versionBody);
    //const data = new FormData(event.target);

    /*const data = new FormData();
    data.append('json', JSON.stringify(versionBody));*/

    const token = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
    const request = {
      headers: { Authorization: 'Bearer ' + token, 'Content-Type': 'application/json' },
      method: 'POST',
      body: data
    };

    fetch('http://localhost:8080/api/subtitles/lines/' + this.props.lineId, request);
  };

  render() {
    return (
      <form onSubmit={this.handleSubmit}>
        <label>
          <input type="text" name="text" className="write_msg" placeholder="Type a version " onChange={this.handleChange} />
          <button className="msg_send_btn" type="submit">
            <i className="fa fa-paper-plane-o" aria-hidden="true" />
          </button>
        </label>
      </form>
    );
  }
}
