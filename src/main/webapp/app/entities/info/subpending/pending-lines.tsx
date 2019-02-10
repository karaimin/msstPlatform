import './pending-lines.scss';
import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getLinesOrigin } from './sub-lines.reducer';

export interface ITranslateInfoProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IActiveLineState {
  showVersions: boolean;
  parentId?: string;
  subtitleId?: string;
}

/*export const initialActiveLines : IActiveLineState = {
  showVersions: false
};*/

export class TranslateInfo extends React.Component<ITranslateInfoProps> {
  activeLines: IActiveLineState = {
    showVersions: false
  };

  constructor(props) {
    super(props);
    this.onVersionClick = this.onVersionClick.bind(this);
  }

  onVersionClick = (aLines: IActiveLineState) => {
    this.activeLines = aLines;
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
                              parentId: line.parentId,
                              subtitleId: this.props.match.params.id
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
                  {this.activeLines.showVersions ? this.activeLines.parentId : 'noooo'}
                  {/*<div className="incoming_msg_img"><img src="https://ptetutorials.com/images/user-profile.png"
                                                         alt="sunil"/></div>
                  <div className="received_msg">
                    <div className="received_withd_msg">
                      <p>Test which is a new approach to have all
                        solutions</p>
                      <span className="time_date"> 11:01 AM | June 9</span>
                    </div>
                  </div>*/}
                </div>
              </div>
              <div className="type_msg">
                <div className="input_msg_write">
                  <input type="text" className="write_msg" placeholder="Type a message" />
                  <button className="msg_send_btn" type="button">
                    <i className="fa fa-paper-plane-o" aria-hidden="true" />
                  </button>
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
