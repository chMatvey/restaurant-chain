import React, {Component} from 'react';
import {Link} from 'react-router-dom';

class Head extends Component {
    render() {
        return (
            <div className="header-component">
                <ul className="link-menu">
                    <li><Link to="/">Home</Link></li>
                    <li><Link to="/chef">Chefs</Link></li>
                    <li><Link to="/timetable">Timetable</Link></li>
                </ul>
            </div>
        )
    }
}

export default Head;
