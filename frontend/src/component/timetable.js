import React, {Component} from 'react';
import {connect} from 'react-redux'
import axios from 'axios';
import {Table} from 'react-bootstrap';

import Head from './part/head'

class Timetable extends Component {
    constructor(props) {
        super(props);
        this.loadTimetable = this.loadTimetable.bind(this);
    }

    componentWillMount() {
        this.loadTimetable();
    }

    loadTimetable() {
        axios.get('/timetable/generate')
            .then(res => {
                const formattedResult = [];
                for (let i = 0; i < 20; i++) {
                    const restaurant = {};
                    const oneRestaurant = [];
                    for (let j = 0; j < 90; j += 3) {
                        const oneDay = [];
                        const day = {};
                        oneDay.push(res.data[i][j].firstChef);
                        oneDay.push(res.data[i][j].secondChef);
                        oneDay.push(res.data[i][j + 1].firstChef);
                        oneDay.push(res.data[i][j + 1].secondChef);
                        oneDay.push(res.data[i][j + 2].firstChef);
                        oneDay.push(res.data[i][j + 2].secondChef);
                        day.id = j/3+1;
                        day.timetable = oneDay;
                        oneRestaurant.push(day);
                    }
                    restaurant.id = i;
                    restaurant.timetable = oneRestaurant;
                    formattedResult.push(restaurant);
                }
                console.log(formattedResult);
                this.props.onGetTimetable(formattedResult);
            })
    }

    render() {
        return (
            <div>
                <Head/>
                {this.props.data.map((restaurant) =>
                    <div id={restaurant.id}>
                        <Table>
                            <thead>
                            <tr>
                                <td>Day</td>
                                <td colSpan={2}>Russian</td>
                                <td colSpan={2}>Italian</td>
                                <td colSpan={2}>Japanese</td>
                            </tr>
                            <tr>
                                <td></td>
                                <td>Morning</td>
                                <td>Evening</td>
                                <td>Morning</td>
                                <td>Evening</td>
                                <td>Morning</td>
                                <td>Evening</td>
                            </tr>
                            </thead>
                            <tbody>
                            {restaurant.timetable.map((day) =>
                                <tr id={day.id}>
                                    <td>{day.id}</td>
                                    {day.timetable.map((chef) =>
                                        <td id={chef.id}>{
                                            chef.lastName + " " +
                                            chef.firstName.substring(0, 1) + "." +
                                            chef.middleName.substring(0, 1) + "."
                                        }</td>
                                    )}
                                </tr>
                            )}
                            </tbody>
                        </Table>
                        <br/>
                    </div>
                )}
            </div>
        )
    }
}

export default connect(
    state => ({
        data: state.timetable,
    }),
    dispatch => ({
        onGetTimetable: (timetable) => {
            dispatch({type: 'ADD', payload: timetable})
        },
    })
)(Timetable);
