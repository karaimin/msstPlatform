import './pending-lines.scss';
import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getLinesOrigin } from './sub-lines.reducer';
import VersionsList from 'app/entities/info/subpending/translated-versions';
import { Storage } from 'react-jhipster';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import SubmitVersionForm from 'app/entities/info/subpending/submit-version';

export interface ITranslateInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IVersionData {
  id?: string;
  version?: string;
  text?: string;
}

export interface IActiveLineState {
  showVersions: boolean;
  parentLineId?: string;
  currentLineId?: string;
  subtitleId?: string;
  versionData: IVersionData[];
  eventSource?: EventSource;
}

export interface SubmitNewVersion {
  version?: string;
}

export const initialActiveLines: IActiveLineState = {
  showVersions: false,
  versionData: []
};

export class TranslateInfo extends React.Component<ITranslateInfoProps, IActiveLineState> {
  constructor(props) {
    super(props);
    this.state = {
      showVersions: false,
      versionData: []
    };

    this.onVersionClick = this.onVersionClick.bind(this);
  }

  onVersionClick = (aLines: IActiveLineState) => {
    // this.state = aLines;
    console.log('setting params' + aLines.currentLineId);
    this.setState({
      showVersions: aLines.showVersions,
      // showVersions: !this.state.showVersions,
      parentLineId: aLines.parentLineId,
      subtitleId: aLines.subtitleId,
      currentLineId: aLines.currentLineId
    });

    this.fetchData(aLines.currentLineId);
    this.listenFroSSEEvents(aLines.currentLineId);
  };

  fetchData = (currentLineId: string) => {
    const token = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
    console.log('token ' + token);
    const request = {
      headers: { Authorization: 'Bearer ' + token }
    };

    const responseData = fetch('http://localhost:8080/api/subtitles/lines/' + currentLineId, request)
      .then(response => response.json())
      .then(data => this.setState({ versionData: data }));
  };

  listenFroSSEEvents = (currentLineId: string) => {
    console.log('props received');
    if (this.state.currentLineId && this.state.currentLineId === currentLineId) {
      return;
    }

    if (this.state.eventSource) {
      this.state.eventSource.close();
      console.log('eventsource closed');
    }

    const eventSource = new EventSource('http://localhost:8080/api/sse/subtitles/lines/' + currentLineId, { withCredentials: false });
    eventSource.onopen = (event: any) => console.log('open', event);
    eventSource.onmessage = (event: any) => {
      const profile = JSON.parse(event.data);

      if (this.state.currentLineId && this.state.currentLineId === currentLineId) {
        console.log('setting state ' + JSON.stringify(profile));
        this.state.versionData.push(profile);
        this.setState({ versionData: this.state.versionData });
        // this.forceUpdate();
      }
    };

    eventSource.onerror = (event: any) => {
      console.log('error', event);
    };

    this.setState({ eventSource: eventSource });
  };

  componentDidMount() {
    this.props.getLinesOrigin(this.props.match.params.id);
  }
  render() {
    const { originLines, match } = this.props;
    return (
      <div className="container">
        <h3 className=" text-center">Messaging</h3>
        <div className="messaging">
          <div className="inbox_msg">
            <div className="inbox_people">
              <div className="inbox_chat">
                <div className="chat_list">
                  {originLines
                    ? originLines.map((line, i) => (
                        <div
                          className="chat_people"
                          onClick={() =>
                            this.onVersionClick({
                              showVersions: true,
                              parentLineId: line.parentLineId,
                              subtitleId: this.props.match.params.id,
                              currentLineId: line.currentLineId,
                              versionData: []
                            })
                          }
                        >
                          <div className="chat_img">
                            <img src="content/images/subtitles/sub1.png" alt="sunil" />
                          </div>
                          <div className="chat_ib">
                            <h5>Line number - {line.sequence + 1}</h5>
                            <p>{line.parentText}</p>
                          </div>
                        </div>
                      ))
                    : ''}
                </div>
              </div>
            </div>
            <div className="mesgs">
              <div className="msg_history">
                <div className="incoming_msg">
                  {this.state.showVersions ? (
                    <VersionsList
                      currentLineId={this.state.currentLineId}
                      showVersions={this.state.showVersions}
                      versionData={this.state.versionData}
                      {...this.props}
                    />
                  ) : (
                    'noooo'
                  )}
                </div>
              </div>
              <div className="type_msg">
                <div className="input_msg_write">
                  <SubmitVersionForm lineId={this.state.currentLineId} {...this.props} />
                </div>
              </div>
            </div>
          </div>
          <p className="text-center top_spac">
            {' '}
            Design by{' '}
            <a target="_blank" href="#">
              Sunil Rajput
            </a>
          </p>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ translateInfo }: IRootState) => ({
  originLines: translateInfo.entities
});

const mapDispatchToProps = {
  getLinesOrigin
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TranslateInfo);
