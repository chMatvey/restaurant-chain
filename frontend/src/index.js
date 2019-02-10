import './index.css';
import * as serviceWorker from './registerServiceWorker';

import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {Router, Route} from 'react-router';
import {Switch} from 'react-router-dom';
import store from './store';
import history from './history';

import Main from './component/main';
import Chef from './component/chef';
import Timetable from './component/timetable';

ReactDOM.render(
    <Provider store={store}>
        <Router history={history}>
            <Switch>
                <Route exact path="/" component={Main}/>
                <Route path="/chef" component={Chef}/>
                <Route path="/timetable" component={Timetable}/>
            </Switch>
        </Router>
    </Provider>,
    document.getElementById('root')
);

serviceWorker.unregister();
