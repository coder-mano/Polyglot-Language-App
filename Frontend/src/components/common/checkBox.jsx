import React from "react";
import { PropTypes } from "prop-types";

const Checkbox = ({
  name,
  label,
  value,
  onChange,
  styleClass,
  error,
  ...rest
}) => {
  const handleChange = (event, ...rest) => {
    // const { checked } = event.target;
    // console.log(event.target);

    onChange({
      name: event.target.name,
      checked: event.target.checked,
      ...rest,
    });
  };
  return (
    <div className={`form-group ${styleClass}`}>
      <label>
        {label}
        <input
          {...rest}
          name={name}
          type="checkbox"
          defaultChecked={value}
          value={value}
          onChange={handleChange}
        />
      </label>
      {error && (
        <div className="alert alert-danger">{JSON.stringify(error)}</div>
      )}
    </div>
  );
};

Checkbox.propTypes = {
  styleClass: PropTypes.string,
  selected: PropTypes.bool.isRequired,
  label: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
};

Checkbox.defaultProps = {
  styleClass: "",
};

export default Checkbox;
