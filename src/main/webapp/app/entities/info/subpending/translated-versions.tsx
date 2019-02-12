import { IActiveLineState, IVersionData } from 'app/entities/info/subpending/pending-lines';
import { RouteComponentProps } from 'react-router';
import React from 'react';

export interface IVersionProps extends IActiveLineState, RouteComponentProps<{ id: string }> {}

export interface IVersionState {
  lineId?: string;
  versions: IVersionData[];
  isRefresh: boolean;
  eventSource?: EventSource;
}

class VersionsList extends React.Component<IVersionProps, IVersionState> {
  // constructor()
  constructor(props) {
    super(props);
    this.state = {
      versions: props.versionData,
      isRefresh: false,
      lineId: props.currentLineId
    };
  }

  /*componentWillReceiveProps(otherProps: Readonly<IVersionProps>) {
    console.log('props received');
    if(this.state.lineId && otherProps.currentLineId === this.state.lineId ){
      return;
    }

    if(this.state.eventSource) {
      this.state.eventSource.close();
      console.log('eventsource closed')
    }

    const eventSource = new EventSource('http://localhost:8080/api/sse/subtitles/lines/' + otherProps.currentLineId, { withCredentials: false });
    eventSource.onopen = (event: any) => console.log('open', event);
    eventSource.onmessage = (event: any) => {
      const profile = JSON.parse(event.data);

      if(this.state.lineId && this.state.lineId === otherProps.currentLineId) {
        console.log('setting state ' + JSON.stringify(profile));
        this.state.versions.push(profile);
        this.setState({versions: this.state.versions, isRefresh: true});
        this.forceUpdate();
      }
    };

    eventSource.onerror = (event: any) => {
      console.log('error', event);
    };

    this.setState({ versions: otherProps.versionData, lineId: otherProps.currentLineId, eventSource: eventSource });
  }

  componentDidMount() {
    this.setState({isRefresh: false})
  }*/

  render() {
    const data = this.props.versionData;
    return (
      <div>
        {data
          ? data.map((version, i) => (
              <div className="incoming_msg">
                <div className="incoming_msg_img">
                  <img src="https://ptetutorials.com/images/user-profile.png" alt="sunil" />
                </div>
                <div className="received_msg">
                  <div className="received_withd_msg">
                    <p>{version.text}</p>
                    <span className="time_date"> 11:01 AM | June 9</span>
                  </div>
                </div>
              </div>
            ))
          : 'iiii'}
      </div>
    );
  }
}

export default VersionsList;
