import React, {Component} from 'react';
import {Carousel} from 'react-bootstrap';
import PropTypes from 'prop-types';

class HomeCarousel extends Component {
    constructor(props) {
        super(props)
    }

    render() {
        return (
            <div className="home-carousel-component">
                <Carousel>
                    {this.props.pictures.map((picture) =>
                        <Carousel.Item>
                            <img id="image-carousel" src={picture.src}/>
                            <Carousel.Caption>
                                <div>
                                    <h1>{picture.title}</h1>
                                </div>
                            </Carousel.Caption>
                        </Carousel.Item>
                    )}
                </Carousel>
            </div>
        )
    }
}

Carousel.propTypes = {
    pictures: PropTypes.array.isRequired
};

export default HomeCarousel;
