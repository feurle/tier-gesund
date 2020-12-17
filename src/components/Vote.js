import React, { Component } from 'react';

class Vote extends Component {
    
    constructor(props) {
        super(props);
        this.state = {
            counter: 0,
            mouseOverCount: 0
        }
    }

    voted = () =>{
        this.setState ({
            counter: this.state.counter + 1
        })
    }

    mouseOver = () => {
        this.setState ({
            mouseOverCount: this.state.mouseOverCount + 1
        })
    }

    render() {
        return (
            <div onClick={this.voted} onMouseOver={this.mouseOver}>
                <h1>{this.props.candidate}</h1>
                <h3>{this.props.address}</h3>
                <h2>{this.state.counter}</h2>
                <h2>{this.state.mouseOverCount}</h2>
            </div>
        );
    }
}

export default Vote;